const userPageButton = document.getElementById("userPage-btn");
const sellerPageButton = document.getElementById("sellerPage-btn");
const adminPageButton = document.getElementById("adminPage-btn");
const logoutButton = document.getElementById("logout-btn");
const refreshButton = document.getElementById("refresh-btn");

function pageClick(res, successURL) {
    if (!res.memberId) {
        alert(res.message);
    } else {
        alert(JSON.stringify(res));
        location.href = successURL;
    }
}

if (userPageButton) {
    userPageButton.addEventListener("click", (event) => {
        const accessToken = localStorage.getItem("ACCESS_TOKEN");
        let headersObj = new Object();
        headersObj["Content-Type"] = "application/json";

        if (accessToken) {
            headersObj["Authorization"] = accessToken;
        }

        fetch(`/user`, {
            method: "GET",
            headers: headersObj,
        }).then((res) => {
            return res.json();
        }).then((res) => {
            pageClick(res, "/userPage")
        }).catch((error) => {
            alert(error);
        });
    })
}

if (sellerPageButton) {
    sellerPageButton.addEventListener("click", (event) => {
        const accessToken = localStorage.getItem("ACCESS_TOKEN");
        let headersObj = new Object();
        headersObj["Content-Type"] = "application/json";

        if (accessToken) {
            headersObj["Authorization"] = accessToken;
        }

        fetch(`/seller`, {
            method: "GET",
            headers: headersObj,
        }).then((res) => {
            return res.json();
        }).then((res) => {
            pageClick(res, "/sellerPage");
        }).catch((error) => {
            alert(error);
        });
    })
}


if (adminPageButton) {
    adminPageButton.addEventListener("click", (event) => {
        const accessToken = localStorage.getItem("ACCESS_TOKEN");
        let headersObj = new Object();
        headersObj["Content-Type"] = "application/json";

        if (accessToken) {
            headersObj["Authorization"] = accessToken;
        }

        fetch(`/admin`, {
            method: "GET",
            headers: headersObj,
        }).then((res) => {
            return res.json();
        }).then((res) => {
            pageClick(res, "/adminPage");
        }).catch((error) => {
            alert(error);
        });
    })
}

if (logoutButton) {
    logoutButton.addEventListener("click", (event) => {
        localStorage.removeItem("ACCESS_TOKEN");
        location.replace("/login");
    })
}

if (refreshButton) {
    console.log("refreshButton");
    refreshButton.addEventListener("click", (event) => {
        fetch(`/v1/access`, {
            method: "POST",
        }).then((res) => {
            if (res.ok) {
                const token = res.headers.get("Authorization");
                localStorage.setItem("ACCESS_TOKEN", token);
            } else {
                throw new Error(`${res.status}`);
            }

            return res.json();
        }).then((res) => {
            if (res.success) {
                alert(res.message);
            } else {
                alert(res.message);
            }
        }).catch((error) => {
            alert(error);
        });
    })
}