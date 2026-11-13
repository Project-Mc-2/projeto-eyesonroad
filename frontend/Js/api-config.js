window.EYESONROAD_API_ORIGIN = location.hostname === "eyesonroad.lab"
    ? "http://api.eyesonroad.lab"
    : "http://localhost:8080";
window.eyesFetch = (url, options = {}) => fetch(url, { credentials: "include", ...options });
