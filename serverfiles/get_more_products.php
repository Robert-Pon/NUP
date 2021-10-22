<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$id2 = $_POST['id1'];
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
if(($i==1 && $id1 = $userID)|| $id1 == "-"){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if(($resp1->fetch_object()->id==$userID)|| $id1 == "-"){
        $getInfo = "SELECT b.* FROM books AS b JOIN people AS p ON p.id = b.person_id WHERE NOT b.person_id = '$userID' AND b.reservation = '0'  ORDER BY -p.nups" ;
        $getInfoR = mysqli_query($conn, $getInfo);
        $responseObject = array();
        $i3 = -1;
        while($res = $getInfoR-> fetch_object()){
            $i3 ++;
            if(intval($res->id) > intval($id2)){
                array_push($responseObject, $res);
            }
        }      
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode($responseObject);
?>