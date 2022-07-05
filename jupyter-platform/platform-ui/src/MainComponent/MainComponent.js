import React, {useState, useEffect} from "react";
import {Table, Button, Modal, Form} from 'react-bootstrap';
import 'antd/dist/antd.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {custosService} from "../custos-service";
import {useNavigate} from "react-router-dom";

const MainComponent = () => {
    const navigate = useNavigate();
    const [table_data1, setData] = useState([]);
    const [table_data2, setData2] = useState([]);
    const [nb_name, setName] = useState([]);

    const [selected_archive, setSelectedArchive] = useState([]);

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const [showLaunchingFromArchive, setShowLaunchingFromArchive] = useState(false);
    const handleCloseLaunchingFromArchive = () => setShowLaunchingFromArchive(false);
    const handleShowLaunchingFromArchive = () => setShowLaunchingFromArchive(true);

    const [notebookStopProcessing, setNotebookStopProcessing] = useState({});
    const [notebookLaunchProcessing, setNotebookLaunchProcessing] = useState({});
    const [archiveLaunchProcessing, setArchiveLaunchProcessing] = useState({});

    const checkAuthentication = () => {
        if (!custosService.identity.authenticated()) {
            navigate("/");
        }
    }

    useEffect(() => {
        checkAuthentication();

        const intervalId = setInterval(() => {
            checkAuthentication();
        }, 2000);

        return () => clearInterval(intervalId);
    }, [useState])

    const handleModalLaunch = async () => {
        const data = {
            name: nb_name,
            createdTime: Date().toLocaleString(),
            cpu: 0.5,
            memory: 1024
        }

        try {
            const res = await fetch('http://localhost:8080/nb/', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${custosService.identity.accessToken}`
                },
                body: JSON.stringify(data),
            });
            const nb = await res.json();
            launchNotebook(nb.id);
            handleClose();
        } catch (e) {
            // TODO
        }
    }

    const launchNotebook = async (recordId) => {
        setNotebookLaunchProcessing({...notebookLaunchProcessing, [recordId]: true});

        try {
            await fetch("http://localhost:8080/nb/launch/" + recordId, {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${custosService.identity.accessToken}`
                }
            });
        } catch (e) {
            // TODO
        }
    }

    const openNotebook = (record) => {
        var newPageUrl = "http://localhost:" + record.bindPort + "/?token=" + record.token;
        window.open(newPageUrl, "_blank")
    }


    const stopNotebook = async (record) => {
        setNotebookStopProcessing({...notebookStopProcessing, [record.id]: true});

        try {
            await fetch("http://localhost:8080/nb/kill/" + record.id, {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${custosService.identity.accessToken}`
                }
            })
        } catch (e) {
            // TODO
        }

    }

    const refreshNotebooks = async () => {
        try {
            let res = await Promise.all([
                fetch("http://localhost:8080/nb/", {
                    method: 'GET',
                    headers: {
                        'Content-type': 'application/json',
                        'Authorization': `Bearer ${custosService.identity.accessToken}`
                    }
                }),
                fetch("http://localhost:8080/nb/launched/", {
                    method: 'GET',
                    headers: {
                        'Content-type': 'application/json',
                        'Authorization': `Bearer ${custosService.identity.accessToken}`
                    }
                })
            ]);

            const nbs = await res[0].json();
            const launched = await res[1].json();

            nbs.forEach(nb => {
                launched.forEach(launch => {
                    if (nb.id === launch.notebookId) {
                        nb["launched"] = true;
                        nb["bindPort"] = launch.bindPort;
                        nb["token"] = launch.token;
                    }
                });

                if (nb["launched"] && notebookLaunchProcessing[nb.id]) {
                    setNotebookLaunchProcessing({...notebookLaunchProcessing, [nb.id]: false});
                }

                if (!nb["launched"] && notebookStopProcessing[nb.id]) {
                    setNotebookStopProcessing({...notebookStopProcessing, [nb.id]: false});
                }
            });

            setData(nbs);
        } catch (e) {
            // TODO
        }
    };

    const refreshArchives = async () => {
        try {
            const res = await fetch("http://localhost:8080/archive/", {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${custosService.identity.accessToken}`
                }
            });
            const archives = await res.json();
            setData2(archives);
        } catch (e) {
            // TODO
        }
    };

    useEffect(() => {
        refreshNotebooks();
        refreshArchives();

        const intervalId = setInterval(() => {
            refreshNotebooks();
            refreshArchives();
        }, 10000);

        return () => clearInterval(intervalId);
    }, [notebookLaunchProcessing, notebookStopProcessing, useState]);


    const launchFromArchive = (archive) => {
        setSelectedArchive(archive);
        handleShowLaunchingFromArchive();
    }

    const handleLaunchFromArchive = async (archive) => {
        const data = {
            name: nb_name + ". Created from Archive : " + selected_archive.description,
            createdTime: Date().toLocaleString(),
            cpu: 0.5,
            memory: 1024,
            archiveId: selected_archive.id
        }


        setArchiveLaunchProcessing({...archiveLaunchProcessing, [archive.id]: true});

        try {
            const res = await fetch('http://localhost:8080/nb/', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${custosService.identity.accessToken}`
                },
                body: JSON.stringify(data),
            });
            const nb = await res.json();
            refreshNotebooks();
            launchNotebook(nb.id);
            setArchiveLaunchProcessing({...archiveLaunchProcessing, [archive.id]: false});
            handleCloseLaunchingFromArchive();
        } catch(e) {
            setArchiveLaunchProcessing({...archiveLaunchProcessing, [archive.id]: false});
            handleCloseLaunchingFromArchive();
        }
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

            <Modal show={showLaunchingFromArchive} onHide={handleClose}>
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
                    <Button variant="secondary" onClick={handleCloseLaunchingFromArchive}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleLaunchFromArchive}>
                        Launch
                    </Button>
                </Modal.Footer>
            </Modal>

            <div className="w-100 d-flex flex-row pb-3">
                <h2 className="flex-fill">Notebooks</h2>
                <Button variant="dark" onClick={handleShow}>
                    <i className='fa fa-plus-circle'/> Launch a Notebook
                </Button>
            </div>
            <table className="table table-striped table-sm align-middle">
                <thead>
                <tr>
                    <th>Notebook Id</th>
                    <th>Name</th>
                    <th>Owner</th>
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
                            <td>{item.owner}</td>
                            <td>{item.createdTime}</td>
                            <td>{item.cpu}</td>
                            <td>{item.memory}</td>
                            <td>{item.launched
                                ? (<>
                                    {!!notebookStopProcessing[item.id] ?
                                        (<button className=' btn btn-sm btn-danger ' disabled={true}
                                                 onClick={() => stopNotebook(item)}>
                                        <span className="spinner-border spinner-border-sm" role="status"
                                              aria-hidden="true"/>
                                            Stopping
                                        </button>) :
                                        (<button className=' btn btn-sm btn-danger '
                                                 onClick={() => stopNotebook(item)}>
                                            <i className='fa fa-stop'/> Stop
                                        </button>)
                                    }

                                    <button className=' btn btn-sm btn-primary ' onClick={() => openNotebook(item)}>
                                        <i className='fa fa-chevron-circle-right'/> Open
                                    </button>
                                </>) :
                                !!notebookLaunchProcessing[item.id] ?
                                    (<button className=' btn btn-sm btn-dark ' disabled={true}
                                             onClick={() => launchNotebook(item.id)}>
                                        <span className="spinner-border spinner-border-sm" role="status"
                                              aria-hidden="true"/>
                                        Launching
                                    </button>) :
                                    (<button className=' btn btn-sm btn-dark '
                                             onClick={() => launchNotebook(item.id)}>
                                        <i className='fa fa-share'/> Launch
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
                                    (<button className=' btn btn-sm btn-dark' disabled={true}
                                             onClick={() => launchFromArchive(item)}>
                                        <span className="spinner-border spinner-border-sm" role="status"
                                              aria-hidden="true"/>
                                        Launching
                                    </button>) :
                                    (<button className=' btn btn-sm btn-dark '
                                             onClick={() => launchFromArchive(item)}>
                                        <i className='fa fa-share'/> Launch
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
