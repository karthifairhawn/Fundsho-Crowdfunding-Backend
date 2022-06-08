const axios = require('axios').default;

const csvFilePath='reqs.csv';

const csv=require('csvtojson')

csv()
.fromFile(csvFilePath)
.then((jsonObj)=>{
    for(var key in jsonObj)
    createUser(jsonObj[key]);
    // console.log(jsonObj[key]);
})

createUser();
function createUser(json){    

    axios.post('https://fundshop-api.herokuapp.com/requests?sessionKey=1084790249', json 
    )
    .then(function (response) {
        console.log(response.status);
    })
    .catch(function (error) {
        console.log(error);
    });

}
