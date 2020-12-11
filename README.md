### Building

**[Important]**: Make sure JAVA_HOME is defined with **Java 11** JDK path.
Fields **spring.datasource.username** and **spring.datasource.password** must be configured on application.properties
 with appropriate values. 

In project's root directory: 

```
mvn clean install -DskipTests
```
### Initializing API on tests enviroment

In project's root directory: 
```
./mvnw spring-boot:run
```
### API Endpoints

These endpoints accept arguments as JSON in the request body and return results as JSON in the response body. 
JSON field names are case insensitive.


**[POST] /health-care-institution/create**

- url structure
```
http://hostname[:port]/health-care-institution/create
```
- JSON parameters
    - **Name**: *String* 
    - **CNPJ**: *String* 
 - example: 
 ```
{ 
    "Name": "HCI",
    "CPNJ": "94.304.732/0001-19"
}
```
- returns
    - **[HTTP 201]** In case of success it returns the same JSON data of the request body.
    
- errors
    - **[HTTP 400]** HCIAlreadyExistsException, InvalidCNPJException, InvalidHCIDataInputException
    - **[HTTP 404]** HCINotFoundException
    

**[POST] /exam/create**

- url structure
```
http://hostname[:port]/exam/create
```
- JSON parameters
    - **healthcareinstitution**: *JSON* Health Care Institution JSON structure as in /health-care-institution/create
    - **patientname**: *String*
    - **patientAge**: *Integer* 
    - **patientGender**: *String*
    - **physicianName**: *String*
    - **physicianCRM**: *String*
    - **procedureName**: *String*
 - example: 
 ```
{
"healtharenstitution": { 
    "Name": "HCI",
    "CPNJ": "94.304.732/0001-19"
    },
"patientName": "John Doe",
"patientAge": 27
"patientGender": "Male"
"physicianName": "Dr. Brown"
"physicianCRM": "123456"
"procedureName: "Chest radiograph"
}
```
- returns
    - **[HTTP 201]** In case of success it returns the same JSON data of the request body with the inclusion of fields **id** and **version**.
    
- errors
    - **[HTTP 400]** InvalidExamDataInputException, OutOfBudgetExeption
    - **[HTTP 404]** HCINotFoundException
    
**[GET] /exam/{exam_id}**

- url structure
```
http://hostname[:port]/exam/create/{exam_id}
```
- path parameters
    - **{exam_id}** : *Integer* Exam identification number
- JSON parameters
   - **Name**: *String* 
   - **CNPJ**: *String* 
 - example: 
 ```
{
"healtharenstitution": { 
    "Name": "HCI",
    "CPNJ": "94.304.732/0001-19"
    }
}
```
- returns
    - **[HTTP 200]** Exam JSON as the request /exam/create with the inclusion of fields **id** and **version**.
    
- errors
    - **[HTTP 400]** OutOfBudgetExeption
    - **[HTTP 404]** HCINotFoundException, ExamNotFoundException
    
**[DELETE] /exam/{exam_id}/delete**

- url structure
```
http://hostname[:port]/exam/create/{exam_id}/delete
```
- path parameters
    - **{exam_id}** : *Integer* Exam identification number
- JSON parameters
   - **Name**: *String* 
   - **CNPJ**: *String* 
 - example: 
 ```
{
"healtharenstitution": { 
    "Name": "HCI",
    "CPNJ": "94.304.732/0001-19"
    }
}
```
- returns
    - **[HTTP 200]** Exam JSON as the request /exam/create with the inclusion of fields **id** and **version**.
    
- errors
    - **[HTTP 400]** OutOfBudgetExeption
    - **[HTTP 404]** HCINotFoundException, ExamNotFoundException
    
**[POST] /exam/{exam_id}/update**

- url structure
```
http://hostname[:port]/exam/create/{exam_id}/delete
```
- path parameters
    - **{exam_id}** : *Integer* Exam identification number
- JSON parameters
    - **id** *Integer* Exam identification number
    - **version** *Integer* version identification number retrieved from /exam/{exam_id} resopnse body
    - **healthcareinstitution**: *JSON* Health Care Institution JSON structure as in /health-care-institution/create
    - **patientname**: *String*
    - **patientAge**: *Integer* 
    - **patientGender**: *String*
    - **physicianName**: *String*
    - **physicianCRM**: *String*
    - **procedureName**: *String*
 - example: 
 ```
"id": 123,
"version": 2,
{
"healtharenstitution": { 
    "Name": "HCI",
    "CPNJ": "94.304.732/0001-19"
    },
"patientName": "John Doe",
"patientAge": 27,
"patientGender": "Male",
"physicianName": "Dr. Brown",
"physicianCRM": "123456",
"procedureName: "Chest radiograph"
}
```
- returns
    - **[HTTP 200]** In case of success it returns the same JSON data of the request body with the new **version** number.
    
- errors
    - **[HTTP 400]** OutOfBudgetExeption
    - **[HTTP 404]** HCINotFoundException, ExamNotFoundException