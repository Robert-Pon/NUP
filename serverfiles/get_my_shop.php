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
        $getInfo1 = "SELECT my_shopping FROM people WHERE id = '$id1'";
        $getInfoR1 = mysqli_query($conn, $getInfo1);
        $responseObject1 = json_decode($getInfoR1->fetch_object()->my_shopping);
        $i3 = 0;
        $responseObject = array();
        while($i3<count($responseObject1)){
            $getInfoR1 = mysqli_query($conn, "SELECT * FROM books WHERE id = '$responseObject1[$i3]'");
            $fetch =  $getInfoR1->fetch_object();
            $fetch -> buyType = $responseObject1[$i3+1]; 
            array_push($responseObject, $fetch);
            $i3 += 2;
        }
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(($responseObject));
?>