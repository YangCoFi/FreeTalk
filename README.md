# FreeTalk
### A community where you can speak freely
## 1.1 Community Homepage
   > TIPS:Any feature can be broken into several requests. 
   * Showing 10 posts
   * Paging display posts
## 2.1 Login Module
### 2.1.1 Mail Delivery
* setting of the mail
    > Start the client SMTP service
* Spring Email
    > Import jar package
    >
    > Mailbox parameter configuration
    >
    > Send mail using **JavaMailSender**
* Template Engine
    > Send HTML mail using **Thymeleaf**
### 2.1.2 Registration Function

> Front desk registration logic If the password cannot be less than a few digits, the username cannot be empty, and so on. These are verified with the bootstrap4 form, as follows:
>
>   div class="**invalid-feedback**"
>
> https://blog.csdn.net/j84491135/article/details/80433016

### 2.1.3 Session Management 
    
### 2.1.4 Generate verification code
> import Package: Kaptcha
* Generate random characters
* Generate Pic

### 2.1.5 Login and Logout
### 2.1.6 Display Login Information

**Interceptor**

Solve problems with a very low degree of coupling
    
use it to accomplish:
At the beginning of the request is a query to the logged in user

> We do not store user information in the browser at first, which is more sensitive. We save a ticket

 We upload the avatar directly in the Controller layer, not to the Service layer.
 Because MultipartFile is an object belonging to the SpringMVC presentation layer. The business layer only processes the update path so that a service can do it.
 
 ### 2.1.7 Check login status
For example, when the user is not logged in, the localhost:8080/community/user/setting in the browser will also enter the response page. This is a loophole in the system, so you need to check the login status.

Custom annotations: 
Idea: Intercept it with an annotation, and don't intercept it without annotation.

Custom annotation

**Use annotations to identify if they need to be logged in to access**


静态页面static可以直接访问，不用写方法。