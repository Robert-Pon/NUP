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
if($i==1 && $id1 = $userID){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){
        $getInfo = "SELECT reservation FROM books WHERE id = '$id2'";
        $getInfoR = mysqli_query($conn, $getInfo);
        $responseObject = $getInfoR->fetch_object()->reservation;
        
        $getInfo1 = "SELECT my_shopping FROM people WHERE id = '$id1'";
        $getInfoR1 = mysqli_query($conn, $getInfo1);
        $responseObject1 = json_decode($getInfoR1->fetch_object()->my_shopping);
        array_push($responseObject1, $id2);
        array_push($responseObject1, "1");
        if($responseObject == "0"){
            mysqli_query($conn, "UPDATE books SET reservation = '6', seen = 'false', res_id = '$id1'  WHERE id = '$id2' AND reservation = '0'");
            $res1 = mysqli_query($conn, "SELECT res_id FROM books WHERE id = '$id2'");
            $res_id = $res1->fetch_object()->res_id;
            if($res_id ==  $userID){
                mysqli_query($conn, "UPDATE people SET my_shopping = '".json_encode($responseObject1)."' WHERE id = '$id1'");
                $responseObject = array("type" => "107", "id" => strval($i));
            }else{
                $responseObject = array("type" => "110", "id" => strval($res_id));
            }
        }else{
            $responseObject = array("type" => "106", "id" => strval($i));
        }
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode($responseObject);
?>