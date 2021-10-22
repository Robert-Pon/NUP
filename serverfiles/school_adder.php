<?php
$password = $_POST['password'];
$login = $_POST['login'];
$name = $_POST['name'];
$number = $_POST['number'];
$type = $_POST['type'];
$x = $_POST['x'];
$y = $_POST['y'];
$mID = $_POST['mID'];
$oryginal = $_POST['original'];
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
if($i>=1 ){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){
        $i=0;
        $id1 = 0;
        //$sql = "SELECT id FROM schools WHERE name = '$name' AND number = '$number' AND type = '$type'";
        $sql = "SELECT * FROM localizations WHERE x = '$x' AND y = '$y'";
        $response = mysqli_query($conn, $sql);
        

        while($resp = $response -> fetch_object()){
            $i++;
            $id1 = $resp -> id;
        }

        if($i==0){
            $sql = "INSERT INTO schools (mID , number , type , name , oryginal) VALUES ('$mID', '$number', '$type', '$name', '$oryginal')";
            $response = mysqli_query($conn, $sql);
            $response = mysqli_query($conn, "SELECT id FROM schools WHERE name = '$name' AND number = '$number' AND type = '$type' AND mID = '$mID'");
            $id1 = $response->fetch_object()->id;
            $sql1 = "INSERT INTO localizations (id, x, y, type) VALUES ('$id1', '".doubleval($x)."', '".doubleval($y)."', '$type')";
            $response = mysqli_query($conn, $sql1);   
            $sql2 = "UPDATE people SET school = '$id1' WHERE id = '$userID'";
            mysqli_query($conn, $sql2);
            $responseObject = array('type' => "100", 'id' => "3");
        }else{
    
            $sql2 = "UPDATE people SET school = '$id1' WHERE id = '$userID'";
            mysqli_query($conn, $sql2);
            $responseObject = array('type' => "113", 'id' => "3");
        }
        $responseObject = array("type" => "109", "id" => strval($sql));
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>
