import React, {useState, useEffect} from "react";
import {useSearchParams, useNavigate} from 'react-router-dom';
import {custosService} from "../custos-service";

export default function () {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    useEffect(() => {
        const code = searchParams.get("code");
        custosService.identity.getToken({code})
            .then(() => {
                navigate("/dashboard");
            }).catch(setError);
    }, []);

    return <div>
        {error?
            (<div>Unauthorized.</div>):
            (<div>Authenticating....</div>)
        }
    </div>
}