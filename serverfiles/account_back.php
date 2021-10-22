<?php
$email = $_POST['email'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "SELECT login, id FROM people WHERE email = '$email'";
$id = mysqli_query($conn, $sql);
$i = 0;
$userID = 0;
$userID = "";
$responseObject = array("type" => "103", "id" => "-");
while($res = $id -> fetch_object()){
 $i++;
 $userID = $res -> id;
 $login = $res -> login;
}
if($i==1){
    $new_password = "";
    $length = rand(9, 20);
    for($i2 = 0; $i2 < $length; $i2++){
        $alphabet = str_split("abcdefghijklmnopqrstuvwxyz");
        $alphabetBIG = str_split(strtoupper("abcdefghijklmnopqrstuvwxyz"));
        $num = str_split("0123456789");
        $type = rand(1, 3);
        switch($type){
            case 1:
                $num1 = rand(0, count($alphabet)-1);
                $new_password = $new_password.$alphabet[$num1];
                break;
              case 2:
                $num1 = rand(0, count($alphabet)-1);
                $new_password = $new_password.$alphabetBIG[$num1];
                break;
              case 3:
                $num1 = rand(0,9);
                $new_password = $new_password.$num[$num1];
                break;
                
        }
    }

    $set = "UPDATE passwords SET password = '$new_password' WHERE id = '$userID'";
    mysqli_query($conn, $set);

    $message = "Odzyskiwanie hasła dla kąta o loginie: $login.\n Nowe hasło to: $new_password. Zalecamy zmienić hasło po odzyskaniu konta.";
   mail($email, "Odzyskiwanie konta", $message);

}else{
}
echo json_encode($responseObject);
?>