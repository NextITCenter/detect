<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>언어 문장 판별</title>
</head>
<body>
<form method="post" action="/detect">
    <label>
        <textarea name="text" rows="8" cols="40">${text}</textarea>
    </label>
    <div>
        <button type="submit">판정</button>
    </div>
</form>
<h3>판정 결과</h3>
<div id="result">${msg}</div>
</body>
</html>