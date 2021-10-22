<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$id2 = $_POST['id1'];
$id3 = $_POST['id3'];
$pages = $_POST['pages'];
$size = $_POST['size'];
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
        mysqli_query($conn, "UPDATE books SET seen = 'true', reservation = '2' WHERE reservation = '1' AND id = '$id3'");
        $pages = intval(doubleval($pages)*0.3);
        $getInfo1 = "SELECT my_shopping FROM people WHERE id = '$id2'";
        $getInfoR1 = mysqli_query($conn, $getInfo1);
        $responseObject1 = json_decode($getInfoR1->fetch_object()->my_shopping);
        $i3 = 0;
        while(($responseObject1[$i3]== $id3 && $responseObject1[$i3+1]!="1") || $responseObject1[$i3]!= $id3){
                $i3 += 2;
            
        }
        $responseObject1[$i3+1] = "3";
        mysqli_query($conn, "UPDATE people SET nups = nups+$pages WHERE id = '$userID'");
        mysqli_query($conn, "UPDATE people SET nups = nups+$pages, my_shopping = '".json_encode($responseObject1)."' WHERE id = '$id2'");
        $responseObject = array("type" => "101", "nups" => strval($pages));
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>