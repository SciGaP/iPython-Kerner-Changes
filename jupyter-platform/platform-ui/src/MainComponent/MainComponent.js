import React, {useState, useEffect} from "react";
import {Table, Button, Modal, Form} from 'react-bootstrap';
import 'antd/dist/antd.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const MainComponent = () => {
    const [table_data1, setData] = useState([]);
    const [table_data2, setData2] = useState([]);
    const [nb_name, setName] = useState([]);

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const [notebookStopProcessing, setNotebookStopProcessing] = useState({});
    const [notebookLaunchProcessing, setNotebookLaunchProcessing] = useState({});
    const [archiveLaunchProcessing, setArchiveLaunchProcessing] = useState({});

    const handleModalLaunch = () => {
        const data = {
            name: nb_name,
            createdTime: Date().toLocaleString(),
            cpu: 0.5,
            memory: 1024
        }

        fetch('http://localhost:8080/nb/', {
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
            },
            body: JSON.stringify(data),
        })
            .then((res) =>
                res.json().then(res => {
                    fetch("http://localhost:8080/nb/launch/" + res.id, {
                        method: 'GET',
                        headers: {
                            'Content-type': 'application/json',
                        }
                    }).then((res) => {
                            console.log(res)
                            handleClose()

                            refreshNotebooks();

                        }
                    );
                }));
    }

    const launchNotebook = (recordId) => {
        setNotebookLaunchProcessing({...notebookLaunchProcessing, [recordId]: true});

        fetch("http://localhost:8080/nb/launch/" + recordId, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json',
            }
        }).then((res) => {
            console.log(res)
            refreshNotebooks();
            setNotebookLaunchProcessing({...notebookLaunchProcessing, [recordId]: false});
        }).catch(() => {
            setNotebookLaunchProcessing({...notebookLaunchProcessing, [recordId]: false});
        });
    }

    const openNotebook = (record) => {
        var newPageUrl = "http://localhost:" + record.bindPort + "/?token=" + record.token;
        window.open(newPageUrl, "_blank")
    }


    const stopNotebook = (record) => {
        setNotebookStopProcessing({...notebookStopProcessing, [record.id]: true});

        fetch("http://localhost:8080/nb/kill/" + record.id, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json',
            }
        }).then((res) => {
            console.log(res)
            refreshNotebooks();
            setNotebookStopProcessing({...notebookStopProcessing, [record.id]: false});
        }).catch(() => {
            setNotebookStopProcessing({...notebookStopProcessing, [record.id]: false});
        });

    }

    const refreshNotebooks = () => {
        fetch("http://localhost:8080/nb/", {
            method: 'GET',
            headers: {
                'Content-type': 'application/json',
            }
        }).then((res) =>
            res.json().then((nbs) => {

                fetch("http://localhost:8080/nb/launched/", {
                    method: 'GET',
                    headers: {
                        'Content-type': 'application/json',
                    }
                }).then((res) =>
                    res.json().then((launched) => {
                        nbs.forEach(nb => {
                            launched.forEach(launch => {
                                if (nb.id === launch.notebookId) {
                                    nb["launched"] = true;
                                    nb["bindPort"] = launch.bindPort;
                                    nb["token"] = launch.token;
                                }
                            })
                        })

                        setData(nbs)
                    })
                );

            })
        );
    };

    const refreshArchives = () => {
        fetch("http://localhost:8080/archive/", {
            method: 'GET',
            headers: {
                'Content-type': 'application/json',
            }
        }).then((res) =>
            res.json().then((data) => setData2(data))
        );
    };

    useEffect(() => {
        refreshNotebooks();
        refreshArchives();
    }, []);


    const launchFromArchive = (archive) => {
        const data = {
            name: "Notebook from Archive : " + archive.description,
            createdTime: Date().toLocaleString(),
            cpu: 0.5,
            memory: 1024,
            archiveId: archive.id
        }


        setArchiveLaunchProcessing({...archiveLaunchProcessing, [archive.id]: true});

        fetch('http://localhost:8080/nb/', {
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
            },
            body: JSON.stringify(data),
        }).then((res) =>
            res.json().then(res => {
                refreshNotebooks();
                launchNotebook(res.id);
                setArchiveLaunchProcessing({...archiveLaunchProcessing, [archive.id]: false});
            }).catch(() => {
                setArchiveLaunchProcessing({...archiveLaunchProcessing, [archive.id]: false});
            }));
    }

    return (
        <>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Launch Notebook</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Label htmlFor="nbNameLbt">Name</Form.Label>
                    <Form.Control
                        type="text"
                        id="nbName"
                        onChange={(Event) => setName(Event.target.value)}
                    />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleModalLaunch}>
                        Launch
                    </Button>
                </Modal.Footer>
            </Modal>

            <div className="w-100 d-flex flex-row pb-3">
                <h2 className="flex-fill">Notebooks</h2>
                <Button variant="dark" onClick={handleShow}>
                    <i className='fa fa-plus-circle'></i> Launch a Notebook
                </Button>
            </div>
            <table className="table table-striped table-sm align-middle">
                <thead>
                <tr>
                    <th>Notebook Id</th>
                    <th>Name</th>
                    <th>Created Time</th>
                    <th>CPU</th>
                    <th>Memory</th>
                    <th style={{minWidth: 200}}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {
                    table_data1.map((item) => (
                        <tr key={item.id} className='align-items-center'>
                            <td>{item.id}</td>
                            <td>{item.name}</td>
                            <td>{item.createdTime}</td>
                            <td>{item.cpu}</td>
                            <td>{item.memory}</td>
                            <td>{item.launched
                                ? (<>
                                    {!!notebookStopProcessing[item.id] ?
                                        (<button className=' btn btn-sm btn-primary ' disabled={true}
                                                 onClick={() => stopNotebook(item)}>
                                        <span className="spinner-border spinner-border-sm" role="status"
                                              aria-hidden="true"/>
                                            Stopping
                                        </button>) :
                                        (<button className=' btn btn-sm btn-danger '
                                                 onClick={() => stopNotebook(item)}>
                                            <i className='fa fa-stop'></i> Stop
                                        </button>)
                                    }

                                    <button className=' btn btn-sm btn-primary ' onClick={() => openNotebook(item)}>
                                    <i className='fa fa-chevron-circle-right'></i> Open
                                    </button>
                                </>) :
                                !!notebookLaunchProcessing[item.id] ?
                                    (<button className=' btn btn-sm btn-primary ' disabled={true}
                                             onClick={() => launchNotebook(item.id)}>
                                        <span className="spinner-border spinner-border-sm" role="status"
                                              aria-hidden="true"/>
                                        Launching
                                    </button>) :
                                    (<button className=' btn btn-sm btn-dark '
                                             onClick={() => launchNotebook(item.id)}>
                                        <i className='fa fa-share'></i> Launch
                                    </button>)
                            }</td>
                        </tr>
                    ))
                }
                </tbody>
            </table>

            <div className="w-100 py-3">
                <h2>Archives</h2>
            </div>

            <table className="table table-striped table-sm align-middle">
                <thead>
                <tr>
                    <th>Archive Id</th>
                    <th>Path</th>
                    <th>Description</th>
                    <th style={{minWidth: 200}}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {
                    table_data2.map((item) => (
                        <tr key={item.id}>
                            <td>{item.id}</td>
                            <td>{item.path}</td>
                            <td>{item.description}</td>
                            <td>
                                {!!archiveLaunchProcessing[item.id] ?
                                    (<button className=' btn btn-sm btn-primary' disabled={true}
                                             onClick={() => launchFromArchive(item)}>
                                        <span className="spinner-border spinner-border-sm" role="status"
                                              aria-hidden="true"/>
                                        Launching
                                    </button>) :
                                    (<button className=' btn btn-sm btn-dark '
                                             onClick={() => launchFromArchive(item)}>
                                        <i className='fa fa-share'></i> Launch
                                    </button>)
                                }
                            </td>
                        </tr>
                    ))
                }
                </tbody>
            </table>
        </>
    );
};

export default MainComponent;
