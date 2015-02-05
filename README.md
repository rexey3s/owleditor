# Ontology Web Language Editor
Note : For English User, please check English description below

Giới thiệu : Đây là ứng dụng dùng để thiết kế và phát triển ontology dưới dạng các tài liệu OWL 2.
Ứng dụng được xây dựng ban đầu với mục đích là nơi để trình bày cho tính năng phân loại tự động của ontology
trong đề tài khóa luận tốt nghiệp "Xây dựng ontology phục vụ cho việc phân loại hàng hóa tự động". Ứng dụng được
phát triển dựa trên nền tảng Vaadin Framework và dự án [Spring Vaadin Integration](https://github.com/peholmst/vaadin4spring/).

## Triển khai ứng dụng
* Các file WAR của ứng dụng có thể tải ở [đây](http://owl.chuongdang.com/~theowl/)
* [Live Demo](owl.chuongdang.com:8080/UI)

## Hướng dẫn build ứng dụng từ source trên các OS họ Linux/MacOS

### Cài đặt Java
1. Linux họ Debian có thể cài đặt qua [PPA này](http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html)
2. Bạn phải chắc chắn rằng biến môi trường ``JAVA_HOME``cài đặt.
```
$ echo 'export JAVA_HOME=/usr/java/jdk1.8.0_25' > ~/.bashrc
$ echo 'export PATH=$JAVA_HOME:$PATH' > ~/.bashrc
```
3. Kiếm tra bằng lệnh ``java -version``

### Cài đặt Maven
1. Chọn và tải một bản thực thi (binaries) của Maven ở [đây](http://maven.apache.org/download.cgi)
2. Giả sử thư mục hiện hành là ``~/home/``  
```
$ tar xzvf apache-maven-3.2.5-bin.tar.gz ~/home/
```
3. Đặt các biến môi trường ``M2_HOME``,``M2`` vào ``.bashrc`` (hoặc những file cấu hình dòng lệnh tương đương)
```
$ echo 'export M2_HOME=/usr/local/apache-maven/apache-maven-3.2.5' > ~/.bashrc
$ echo 'export M2=$M2_HOME/bin' > ~/.bashrc
$ echo 'export PATH=$M2:$PATH' > ~/.bashrc
```
4. Kiếm tra ``mvn --version``

### Cài đặt SWRL-API
1. Clone một bản [SWRL-API](https://github.com/protegeproject/swrlapi)
2. Dùng Maven để cài đặt 
```
$ cd ~/swrlapi
~/swrlapi$ mvn clean install
```

### Build tập tin WAR cho ứng dụng OWL Editor
1. Clone source code của [ứng dụng](https://rexey3s@bitbucket.org/rexey3s/spring-vaadin-owleditor.git)
2. Build bằng Maven
```
$ cd ~/spring-vaadin-owleditor
~/spring-vaadin-owleditor$ mvn clean package
```
3. Đợi cho quá trình build xong, kiếm tra thư mục ``src/target`` sẽ có file WAR bên trong có thể triển khai nhanh bằng lệnh
```
~/spring-vaadin-owleditor$ mvn jetty:run
```
Ứng dụng được chạy ở ``localhost:8080/UI``.
 
## Giấy phép
Ứng dụng được phát hành dưới giấy phép  Apache License Version 2.0
