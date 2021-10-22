<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$book_id = $_POST['mID'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "SELECT id FROM people WHERE login = '$login'";
$id = mysqli_query($conn, $sql);
$i = 0;
$userID = 0;
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
        $sql = "SELECT photos FROM books WHERE id = '$book_id'";
        $result1 = mysqli_query($conn, $sql);
        $resp = $result1 -> fetch_object();
        $array = json_decode(strval($resp->photos));
        
        $name = $_FILES['image']['name'];
        if(!file_exists("xIsqweriosamuiwrunsdkjfkaurpwcompress/$id1")){
            mkdir("xIsqweriosamuiwrunsdkjfkaurpwcompress/$id1/$book_id", 0777, true);
            move_uploaded_file($_FILES['image']['tmp_name'], "xIsqweriosamuiwrunsdkjfkaurpwcompress/$id1/$book_id/$name");
        }else if(!file_exists("xIsqweriosamuiwrunsdkjfkaurpwcompress/$id1/$book_id")){
            mkdir("xIsqweriosamuiwrunsdkjfkaurpwcompress/$id1/$book_id", 0777, true);
            move_uploaded_file($_FILES['image']['tmp_name'], "xIsqweriosamuiwrunsdkjfkaurpwcompress/$id1/$book_id/$name");
        }else{
            move_uploaded_file($_FILES['image']['tmp_name'], "xIsqweriosamuiwrunsdkjfkaurpwcompress/$id1/$book_id/$name");
        }
        
        $array[0] = strval("/$id1/$book_id/$name");
        $sql = "UPDATE books SET photos = '".json_encode($array)."' WHERE id = '$book_id'";
        mysqli_query($conn, $sql);
        $responseObject = array("type" => "101", "id" => strval($i));

    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>