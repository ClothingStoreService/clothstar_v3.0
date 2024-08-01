const createButton = document.getElementById("create-btn");
const certifyNumEmailSendButton = document.getElementById("certifyNum-btn");

if (certifyNumEmailSendButton) {
    certifyNumEmailSendButton.addEventListener("click", (event) => {
        const emailValue = document.getElementById("email").value;
        if (emailValue == null || emailValue == "") {
            alert("이메일을 입력해 주세요");
        } else {
            fetch(`/v1/members/auth`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: emailValue
                }),
            }).then((res) => {
                if (res.ok) {
                    alert("인증번호가 전송 되었습니다.")
                }
            }).catch(() => {
                console.log("catch");
            });
        }
    });
}

if (createButton) {
    createButton.addEventListener("click", (event) => {
        fetch("/v1/members", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: document.getElementById("email").value,
                password: document.getElementById("password").value,
                name: document.getElementById("name").value,
                telNo: document.getElementById("telNo").value,
                certifyNum: document.getElementById("certifyNum").value,
            }),
        }).then((res) => res.json())
            .then((res) => {
                if (res.success) {
                    alert(res.message);
                    location.replace("/login");
                } else {
                    alert(res.message);
                }
            }).catch(() => {
            alert("ajax 호출 에러")
        });
    });
}

const emailCheck = () => {
    const emailValue = document.getElementById("email").value;

    if (emailValue) {
        fetch(`/v1/members/email/${emailValue}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        }).then((res) => {
            return res.json()
        }).then((res) => {
            if (res.success) {
                alert(res.message);
            } else {
                alert(res.message);
            }
        }).catch(() => {
            console.log("catch");
        });
    }
}