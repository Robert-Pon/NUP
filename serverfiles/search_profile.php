<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$search = $_POST['search'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "SELECT id FROM people WHERE login = '$login'";
$id = mysqli_query($conn, $sql);
$i = 0;
$userID = 0;
$search = $_POST['search'];
$responseObject = array("type" => "103", "id" => "-");
while($res = $id -> fetch_object()){
 $i++;
 $userID = $res -> id;
}
if(($i==1 && $id1 = $userID)|| $id1 = "-"){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){
        $sql = "SELECT * FROM people WHERE name LIKE '%$search%' OR familyname LIKE '%$search%' OR city LIKE '%$search%' OR description LIKE '%$search%' ORDER BY -nups";
        $getInfoR = mysqli_query($conn, $sql);
        $responseObject = array();
        while($res = $getInfoR-> fetch_object()){
            array_push($responseObject, $res);
        }        
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(($responseObject));
?>