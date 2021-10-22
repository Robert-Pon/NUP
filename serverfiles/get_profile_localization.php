<?php
$login = $_POST['login'];
$password = $_POST['password'];
$school = $_POST['school'];
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
if(($i==1)|| $id1 = "-"){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if(($resp1->fetch_object()->id==$userID)|| $id1 = "-"){
        $getInfo = "SELECT l.* FROM localizations AS l JOIN people AS p ON p.id = $school WHERE l.id = p.school";
        $getInfoR = mysqli_query($conn, $getInfo);
        $responseObject = $getInfoR-> fetch_object();        
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>