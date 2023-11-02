import axios from "axios";
import qs from "qs";

export const apiClient = axios.create({
    //baseURL: "https://api.kookmin-timebank.com",
    baseURL: "/",
    headers: {
        "Content-Type": "application/json",
    },
    paramsSerializer: {
        serialize: (params) => {
            return qs.stringify(params, {
                filter: (prefix, value) => value || undefined,
            });
        },
    }
});

