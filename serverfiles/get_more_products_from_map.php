<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$x = $_POST['x'];
$y = $_POST['y'];
$search = $_POST['search'];

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
        $localizationID = mysqli_query($conn, "SELECT id FROM localizations WHERE x = '".doubleval($x)."' AND y = '".doubleval($y)."'")->fetch_object()->id;
        $sql = "SELECT b.* FROM people AS p ON p.school = '$localizationID' JOIN books AS b ON b.person_id = p.id WHERE b.too_search LIKE '%$search%' AND NOT b.person_id = '$userID' AND b.reservation = '0' ORDER BY -p.nups LIMIT 10";
        $getInfoR = mysqli_query($conn, $sql);
        $responseObject = array();
        $i = 0;
        while($res = $getInfoR-> fetch_object()){
            array_push($responseObject, $res);
        }      

        $responseObject = array_slice($responseObject, 0, count($responseObject)/4);
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode($responseObject);
?> 