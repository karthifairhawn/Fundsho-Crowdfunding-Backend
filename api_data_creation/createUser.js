const axios = require('axios').default;


const csvFilePath='users.csv';

const csv=require('csvtojson')

csv()
.fromFile(csvFilePath)
.then((jsonObj)=>{
    for(var key in jsonObj)
    createUser(jsonObj[key]);
    // console.log(jsonObj[key]);
})


function createUser(jsonObj){    

    axios.post('https://fundshop-api.herokuapp.com/users', {
        fname: jsonObj['fname'],
        lname: jsonObj['lname'],
        dob:   jsonObj['dob'],
        email: jsonObj['email'],
        phNumber: jsonObj['phNumber'],
        password:  jsonObj['password'],
        username: jsonObj['username'],
        avatarUrl: jsonObj['avatarUrl']
    })
    .then(function (response) {
        console.log(response.status);
    })
    .catch(function (error) {
        console.log(error);
    });

}
