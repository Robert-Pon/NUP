<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "SELECT id FROM people WHERE login = '$login'";
$id = mysqli_query($conn, $sql);
$i = 0;
$userID = 0;
echo "D";
echo $login;
$responseObject = array("type" => "103", "id" => "-");
while($res = $id -> fetch_object()){
 $i++;
 $userID = $res -> id;
}
if($i==1 && $id1 = $userID){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){   
        $name = "profile.jpg";
        if(!file_exists("xIsqweriosamuiwrunsdkjfkaurpwprofilescompress/$id1")){
            mkdir("xIsqweriosamuiwrunsdkjfkaurpwprofilescompress/$id1", 0777, true);
            move_uploaded_file($_FILES['image']['tmp_name'], "xIsqweriosamuiwrunsdkjfkaurpwprofilescompress/$id1/profile.jpg");
        }else if(!file_exists("xIsqweriosamuiwrunsdkjfkaurpwprofilescompress/$id1")){
            mkdir("xIsqweriosamuiwrunsdkjfkaurpwprofilescompress/$id1", 0777, true);
            move_uploaded_file($_FILES['image']['tmp_name'], "xIsqweriosamuiwrunsdkjfkaurpwprofilescompress/$id1/profile.jpg");
        }else{
            move_uploaded_file($_FILES['image']['tmp_name'], "xIsqweriosamuiwrunsdkjfkaurpwprofilescompress/$id1/profile.jpg");
        }
        
        $responseObject = array("type" => "101", "id" => strval($i));

    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>