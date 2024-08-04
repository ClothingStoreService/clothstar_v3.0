const loginButton = document.getElementById("login-btn");

if (loginButton) {
    loginButton.addEventListener("click", (event) => {
        fetch("/v1/members/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: document.getElementById("email").value,
                password: document.getElementById("password").value,
            }),
        }).then((res) => {
            if (res.ok) {
                const token = res.headers.get("Authorization");
                localStorage.setItem("ACCESS_TOKEN", token);
            }
            return res.json();
        }).then(res => {
            console.log(res);
            if (res.success) {
                location.replace("/");
            } else {
                alert(res.message);
            }
        }).catch((error) => {
            alert(error);
        });
    })
}