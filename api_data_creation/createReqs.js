const axios = require('axios').default;

createUser();
function createUser(){    

    axios.post('https://fundshop-api.herokuapp.com/requests?sessionKey=1084790249', 
        {
            "fname": "Kippie",
            "lname": "Drance",
            "gender": "Male",
            "background": "quam ",
            "dateOfBirth": "1989-12-28",
            "institutionName": "Cardiff University",
            "studyProgram": "Environmental Tech",
            "institutePlace": "Sainte-Geneviève-des-Bois",
            "additionalEdInfo": "hac ",
            "phoneNumber": 854564614,
            "address": "19 Lakewood Gardens Drive",
            "city": "Póvoa",
            "pinCode": "91709",
            "stateRegion": "Île-de-France",
            "personalEmail": "kdrance0@edublogs.org",
            "eventImageUrl": "http://dummyimage.com/185x161.png/cc0000/ffffff",
            "eventTitle": "tellus semper interdum mauris ullamcorper",
            "amountRequired": 27866,
            "deadLine": "2022-06-08T17:01:37.171Z",
            "addtionalFilesUrl": "http://dummyimage.com/145x235.png/ff4444/ffffff",
            "eventDescription": "ornare",
            "sessionId": ""
        }      
    )
    .then(function (response) {
        console.log(response.status);
    })
    .catch(function (error) {
        console.log(error);
    });

}
