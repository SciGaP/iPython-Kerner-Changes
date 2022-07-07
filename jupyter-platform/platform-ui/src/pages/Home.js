import React, {useState, useEffect} from "react";
import {useSearchParams, useNavigate} from 'react-router-dom';
import {custosService} from "../custos-service";

export default function () {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    return <div className="w-100 h-100 d-flex flex-row">
        <div className="p-2">
            <h2 id="vision">Click Login to continue</h2>
        </div>
    </div>
}