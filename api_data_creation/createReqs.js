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


function createUser(jsonObj){    

    axios.post('https://fundshop-api.herokuapp.com/requests?sessionKey=-1678574516', {
        fname : jsonObj.fname,
        lname : jsonObj.lname,
        gender : jsonObj.gender,
        background : jsonObj.background,
        dateOfBirth : jsonObj.dateOfBirth,

        institutePlace : jsonObj.institutePlace,
        studyProgram : jsonObj.studyProgram,
        institutePlace : jsonObj.institutePlace,
        additionalEdInfo : jsonObj.additionalEdInfo,

        phoneNumber : jsonObj.phoneNumber,
        address : jsonObj.address,
        city : jsonObj.city,
        pinCode : jsonObj.pinCode,
        stateRegion : jsonObj.stateRegion,
        personalEmail : jsonObj.personalEmail,

        eventImageUrl : jsonObj.eventImageUrl,
        eventTitle : jsonObj.eventTitle,
        amountRequired : jsonObj.amountRequired,
        deadLine : jsonObj.deadLine,
        addtionalFilesUrl : jsonObj.addtionalFilesUrl,
        eventDescription : jsonObj.eventDescription,        
    })
    .then(function (response) {
        console.log(response.status);
    })
    .catch(function (error) {
        console.log(error);
    });

}
