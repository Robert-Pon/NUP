<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$id2 = $_POST['id1'];
$name = $_POST['name'];
$class = $_POST['class'];
$subject = $_POST['subject'];
$description = $_POST['description'];
$level = $_POST['level'];
$quality = $_POST['quality'];
$photos = $_POST['photos'];
$price = $_POST['price'];
$pages = $_POST['pages'];
$size = $_POST['size'];
$too_search = $_POST['too_search'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "SELECT id FROM people WHERE login = '$login'";
$id = mysqli_query($conn, $sql);
$i = 0;
$userID = 0;
$responseObject = array("type" => "103", "id" => "-");
while($res = $id -> fetch_object()){
 $i++;
 $userID = $res -> id;
}
if($i==1 && $id1 == $userID){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){
        $add_book = "UPDATE books SET name = '$name' , subject = '$subject', level = '$level', class = '$class', description = '$description', pages = '$pages', size = '$size', quality = '$quality', price = '$price', currency = 'PLN', photos = '$photos', too_search = '$too_search' WHERE id = '$id2'";
        mysqli_query($conn, $add_book);
        $responseObject = array("type" => "105", "id" => strval($i));
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>