import './App.css';
import MainComponent from './MainComponent';
import logo from "./logo.svg"

function App() {
    return (
        <div className="w-100">
            <nav className="navbar navbar-dark bg-dark pl-3 pr-3">
                <a className="navbar-brand" href="#">
                    <img src={logo} width="30" height="30"
                         className="d-inline-block align-top" alt=""/>
                    Airavata Jupyter Platform
                </a>
            </nav>
            <div className="w-100 px-4 py-3">
                <MainComponent/>
            </div>
        </div>
    );
}

export default App;
