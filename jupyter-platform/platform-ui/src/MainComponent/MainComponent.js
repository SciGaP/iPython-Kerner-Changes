import React, { useState, useEffect } from "react";
import { Table, Button, Modal, Form } from 'react-bootstrap';
import 'antd/dist/antd.css';
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const MainComponent = () => {
  const [table_data1, setData] = useState([]);
  const [table_data2, setData2] = useState([]);
  const [nb_name, setName] = useState([]);

  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const handleModalLaunch = () => {
    const data = {
      name  : nb_name,
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
                }}).then((res) => {
                    console.log(res)
                    handleClose()

                  }
              );
            }));
  }

  const launchNotebook = (recordId) => {
    fetch("http://localhost:8080/nb/launch/" + recordId, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
      }}).then((res) => console.log(res)
    );
  }

  const openNotebook = (record) => {
    var newPageUrl = "http://localhost:" + record.bindPort + "/?token=" + record.token;
    window.open(newPageUrl, "_blank")
  }


  const stopNotebook = (record) => {
    fetch("http://localhost:8080/nb/kill/" + record.id, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
      }}).then((res) => console.log(res)
    );

  }

  useEffect(() => {

    fetch ("http://localhost:8080/nb/", {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
      }}).then((res) =>
    res.json().then((nbs) => {

      fetch("http://localhost:8080/nb/launched/", {
        method: 'GET',
        headers: {
          'Content-type': 'application/json',
        }}).then((res) =>
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


    fetch("http://localhost:8080/archive/", {
        method: 'GET',
        headers: {
          'Content-type': 'application/json',
        }}).then((res) =>
      res.json().then((data) => setData2(data))
      );
  }, []);


  const launchFromArchive = (archive) =>  {
    const data = {
        name  : "Notebook from Archive : " + archive.description,
        createdTime: Date().toLocaleString(),
        cpu: 0.5,
        memory: 1024,
        archiveId: archive.id
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
                }}).then((res) => console.log(res)
              );
            }));
  }

  
  return (
    <>
      <Button variant="primary" onClick={handleShow}>
        Launch a Notebook
      </Button>

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

      <div>Notebooks</div>
      <Table striped bordered hover>
        <thead>
        <tr>
          <th>Notebook Id</th>
          <th>Name</th>
          <th>Created Time</th>
          <th>CPU</th>
          <th>Memory</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        {
          table_data1.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>{item.name}</td>
              <td>{item.createdTime}</td>
              <td>{item.cpu}</td>
              <td>{item.memory}</td>
              <td>{ item.launched
                  ? (<>
                    <button className='btn btn-primary' onClick={() => stopNotebook(item)}>Stop</button>
                    <button className='btn btn-primary' onClick={() => openNotebook(item)}>Open</button>
                  </>)
                  : <button className='btn btn-primary' onClick={() => launchNotebook(item.id)}>Launch</button>
              }</td>
            </tr>
          ))
        }
        </tbody>
      </Table>

      <div>Archives</div>

      <Table striped bordered hover>
        <thead>
        <tr>
          <th>Archive Id</th>
          <th>Path</th>
          <th>Description</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        {
          table_data2.map((item) => (
              <tr key={item.id}>
                <td>{item.id}</td>
                <td>{item.path}</td>
                <td>{item.description}</td>
                <td><button className='btn btn-primary' onClick={() => launchFromArchive(item)}>Launch Notebook</button></td>
              </tr>
          ))
        }
        </tbody>
      </Table>
    </>
  );
};

export default MainComponent;
