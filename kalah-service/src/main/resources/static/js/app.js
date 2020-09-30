const userAction = async () => {
  const response = await fetch('http://localhost:8080/games/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  });
  const myJson = await response.json(); //extract JSON from the http response
  console.log("body" + myJson.toString())
}