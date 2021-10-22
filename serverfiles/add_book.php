<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$name = $_POST['name'];
$class = $_POST['class'];
$subject = $_POST['subject'];
$description = $_POST['description'];
$level = $_POST['level'];
$tags = $_POST['tags'];
$quality = $_POST['quality'];
$price = $_POST['price'];
$pages = $_POST['pages'];
$size = $_POST['size'];
$mID = $_POST['mID'];
$exchange = $_POST['exchange'];
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
        $add_book = "INSERT INTO books ( person_id, name, subject, level, class, description, pages, size, quality, photos, price, currency, reservation, tags, mID, seen, res_id, too_search, exchange) VALUES  ('$id1','$name', '$subject', '$level', '$class','$description', '$pages', '$size', '$quality', '".json_encode(array(json_decode("")))."', '$price', 'PLN', '0', '$tags', '$mID','true', '', '$too_search', '$exchange');";
        mysqli_query($conn, $add_book);
        $sql = "SELECT id FROM books WHERE person_id = '$userID' AND mID = '$mID'";
        $result = mysqli_query($conn, $sql);
        $responseObject = array("type" => "105", "id" => strval($result->fetch_object()->id));
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>