<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$name = $_POST['name'];
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
if($i==1 && $id1 = $userID){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){
        $sql = "SELECT * FROM tags WHERE name = '$name'";
        $getInfoR = mysqli_query($conn, $sql);
        $responseObject = "";
        while($res = $getInfoR-> fetch_object()){
            $responseObject = $res;
        }  
        if($responseObject==""){
            $sql = "INSERT INTO tags (name) VALUES ('$name')";
            mysqli_query($conn, $sql);
            $sql = "SELECT * FROM tags WHERE name = '$name'";
            $tagID = mysqli_query($conn, $sql)->fetch_object();
            $responseObject = array($tagID);
        }else{
            $responseObject = array($responseObject);
        }   
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(($responseObject));
?>