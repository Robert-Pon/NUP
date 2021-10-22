<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
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
        $getInfo = "SELECT nups FROM people WHERE id = '$userID'";
        $getInfoR = mysqli_query($conn, $getInfo);
        $responseObject = $getInfoR->fetch_object()->nups;
        $object = array("lvl" => strval(intval($responseObject/75)+1),
        "nups" =>strval($responseObject),
        "pages" => strval($responseObject*2),
        "weight" => strval($responseObject*2*0.0045),
        "trees" => strval(intval(($responseObject*2*0.0045)/(1000/17)*1000))/1000);
       $responseObject = $object;
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>