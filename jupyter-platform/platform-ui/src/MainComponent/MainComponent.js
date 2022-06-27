import React, { useState, useEffect } from "react";
import { Button, Input } from "antd";
import { Space, Table } from 'antd';
import 'antd/dist/antd.css';

const MainComponent = () => {
  const [flag, setFlag] = useState(false);
  const [name, setName] = useState("");
  const [memory, setMemory] = useState("");
  const [cpu, setCpu] = useState("");
  const [response, setResponse] = useState(null);
  const [table_data1, setData] = useState([]);
  const [table_data2, setData2] = useState([]);

  const notebooks_table = [
    {
      title: 'Id',
      dataIndex: 'id',
      key: 'id',
      render: (text) => <a>{text}</a>,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Created Time',
      dataIndex: 'createdTime',
      key: 'createdTime',
    },
    {
      title: 'CPU',
      dataIndex: 'cpu',
      key: 'cpu',
    },
    {
      title: 'Memory',
      dataIndex: 'memory',
      key: 'memory',
    },
    {
      title: 'Archive Id',
      dataIndex: 'archiveId',
      key: 'archiveId',
    },
    {
      title: 'Action',
      key: 'action',
      render: (record) => (
        <Space size="middle">
          { record.launched
              ? <Button onClick={() => stopNotebook(record)}>Stop</Button>
              : <Button onClick={() => launchNotebook(record)}>Launch</Button>
          }

        </Space>
      ),
    },
  ];

  const launchNotebook = (record) => {
    fetch("http://localhost:8080/nb/launch/" + record.id, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
      }}).then((res) => console.log(res)
    );

  }


  const stopNotebook = (record) => {
    fetch("http://localhost:8080/nb/kill/" + record.id, {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
      }}).then((res) => console.log(res)
    );

  }



  const archive_table = [
    {
      title: 'Archive ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Path',
      dataIndex: 'path',
      key: 'path',
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Action',
      key: 'action',
      render: (record) => (
        <Space size="middle">
          <Button onClick={() => run_deleteDB(record)}>Delete</Button>
        </Space>
      ),
    },
  ];

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
  
  const saveDB = () => {
    const data = {
      name: name,
      memory: memory,
      cpu: cpu,
    };


  
    fetch('http://127.0.0.1:5000', {
      method: 'POST',
      headers: {
        'Content-type': 'application/json',
      },
      body: JSON.stringify(data),
    })
      .then((res) =>
      res.json().then(res => setResponse(res)));
  };
  
  return (
    <>
    {!response &&
    <>
    <Button type="primary" onClick={() => setFlag(true)}>
        Create
      </Button>
      {flag && (
        <>
          <Input
            placeholder="Name"
            onChange={(Event) => setName(Event.target.value)}
          />
          <Input
            placeholder="Memory"
            onChange={(Event) => setMemory(Event.target.value)}
          />
          <Input
            placeholder="CPU"
            onChange={(Event) => setCpu(Event.target.value)}
          />
          <Button type="primary" onClick={() => saveDB()}>
            Submit
          </Button>
        </>
      )}
    </>}
      
      {response && 
      <>
        <p>{`Name ${response.name}`}</p>
        <p>{`Memory ${response.memory}`}</p>
        <p>{`CPU ${response.cpu}`}</p>
        </>
         }
        <div>Notebooks</div>
        <Table columns={notebooks_table} dataSource={table_data1} />
        <div>Archives</div>
        <Table columns={archive_table} dataSource={table_data2} />
    </>
  );
};

export default MainComponent;
