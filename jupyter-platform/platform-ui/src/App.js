import './App.css';
import MainComponent from './MainComponent';
import logo from "./logo.svg"
import CustosService, {custosService} from "./custos-service";
import {
    BrowserRouter,
    Routes,
    Route,
    Navigate,
    NavLink
} from "react-router-dom";
import Callback from "./pages/Callback";
import {useEffect, useState} from "react";
import Home from "./pages/Home";

function App() {
    const [loginUrl, setLoginUrl] = useState(null);
    const [authenticated, setAuthenticated] = useState(false);
    const [authenticatedUser, setAuthenticatedUser] = useState(null);
x
    const checkAuthentication = () => {
        if (custosService.identity.authenticated()) {
            setAuthenticated(true);
        } else {
            setAuthenticated(false);
        }
    }

    useEffect(() => {
        checkAuthentication();

        const intervalId = setInterval(() => {
            checkAuthentication();
        }, 2000);

        return () => clearInterval(intervalId);
    }, [useState])

    useEffect(() => {
        custosService.identity.getCILogonAuthenticationUrl().then(url => {
            setLoginUrl(url);
        });
    }, [useState]);

    useEffect(() => {
        if (authenticated) {
            const currentUsername = custosService.identity.currentUsername();
            setAuthenticatedUser({username: currentUsername});

            custosService.users.findUsers({username: currentUsername})
                .then(users => {
                    if (users && users.length > 0) {
                        setAuthenticatedUser(users[0]);
                    }
                });
        }
    }, [authenticated])

    const clickLogout = () => {
        custosService.identity.logout();
    }

    return (
        <div className="w-100 h-100 d-flex flex-column">
            <nav className="navbar navbar-dark bg-dark pl-3 pr-3">
                <a href="/" className="navbar-brand" href="#">
                    <img src={logo} width="30" height="30"
                         className="d-inline-block align-top" alt=""/>
                    Airavata Jupyter Platform
                </a>
                <div className="text-light">
                    {authenticated ?
                        (<div>
                            <div className="d-inline p-3">
                                {authenticatedUser ?
                                    (<span>
                                        <span>{authenticatedUser.first_name}</span>,
                                        <span>{authenticatedUser.last_name}</span>
                                    </span>) :
                                    (<span>Loading...</span>)
                                }
                            </div>

                            <a className="btn btn-outline-info" href="/dashboard">Dashboard</a>
                            <a className="btn btn-outline-light" href={"#"} onClick={clickLogout}>Logout</a>
                        </div>) :
                        (<div>
                            <a className="btn btn-outline-light" href={loginUrl}>Login</a>
                        </div>)
                    }
                </div>
            </nav>

            <div className="w-100 px-4 py-3 flex-fill">
                <BrowserRouter>
                    <Routes>
                        <Route path="/callback" element={<Callback/>}/>
                        <Route path="/dashboard" element={<MainComponent/>}/>
                        <Route path="/" element={<Home/>}/>
                        <Route
                            path="*"
                            element={<Navigate to="/" replace/>}
                        />
                    </Routes>
                </BrowserRouter>
            </div>
        </div>
    );
}

export default App;
