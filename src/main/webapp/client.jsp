<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>

<script language="JavaScript">
    var wsuri = "ws://localhost:8080/echo";
    var ws = null;

    function connectEndpoint() {
        ws = new WebSocket(wsuri);
        ws.onmessage = function(evt) {
            //alert(evt.data);
            document.getElementById("echo").value = evt.data;
        };

        ws.onclose = function(evt) {
            //alert("close");
            document.getElementById("echo").value = "end";
        };

        ws.onopen = function(evt) {
            //alert("open");
            document.getElementById("echo").value = "open";
        };
    }

    function sendmsg() {
        ws.send(document.getElementById("send").value);
    }
</script>
<body onload="connectEndpoint()">
<input type="text" size="20" value="5" id="send">
<input type="button" value="send" onclick="sendmsg()">
<br>
<input type="text" id="echo">
</body>
</html>
