<?php
$id = $_POST['id'];
$login = $_POST['login'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "SELECT * FROM people WHERE login = '$login'";
$response = mysqli_query($conn, $sql);
$array = array();
$responseObject = array('type' => "100", 'id' => $id);
$i=0;
while($resp = $response -> fetch_object()){
    $i++;
}
if($i==0){
    if(count($array)==0){
        if($id!="-"){
            $sql = "SELECT * FROM reservations WHERE login = '$login' AND NOT id = '$id'";
            $response = mysqli_query($conn, $sql);
            while($resp = $response -> fetch_object()){
                $i++;
            }
            if($i==0){
                $sql = "UPDATE reservations SET login = '$login' WHERE id = '$id'";
                mysqli_query($conn, $sql);
                $responseObject = array('type' => "101", 'id' => $id);
            }
        }else{
            $sql = "SELECT * FROM reservations WHERE login = '$login'";
            $response = mysqli_query($conn, $sql);
            $array = array();
            while($resp = $response -> fetch_object()){
                $i++;
            }
            if($i==0){
                $sql = "INSERT INTO reservations (login) VALUES ('$login')";
                mysqli_query($conn, $sql);
                $sql = "SELECT id FROM reservations WHERE login = '$login'";
                $id = (mysqli_query($conn, $sql) -> fetch_object());
                $responseObject = array('type' => "101", 'id' => $id -> id);
            }
        }

    }else{
        
    }
}

echo json_encode(array($responseObject));
?>
