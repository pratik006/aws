curl -X POST -H "Content-Type: application/json" --data @email-service/src/test/resources/testdata/simple-msg-request.json

curl -X POST -H "Content-Type: application/json" --data @src/test/resources/testdata/simple-msg-request.json https://uviwk5z1n9.execute-api.us-east-2.amazonaws.com/test


curl -X POST -H "Content-Type: application/json" --data @src/test/resources/testdata/simple-msg-request.json http://ec2-18-191-73-75.us-east-2.compute.amazonaws.com:8080/email/send