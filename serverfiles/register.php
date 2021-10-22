<?php
$name = $_POST['name'];
$surname = $_POST['surname'];
$login = $_POST['login'];
$city = $_POST['city'];
$school = $_POST['school'];
$email = $_POST['email'];
$password = $_POST['password'];
$idR = $_POST['idR'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "INSERT INTO people (name, familyname ,city, school, photo ,email, login, class, number, description, nups, my_shopping) VALUES ( '$name','$surname','$city','$school','null','$email', '$login', '','','',0, '".json_encode(array())."')";
mysqli_query($conn, $sql);
$select  = mysqli_query($conn, "SELECT id FROM people WHERE login = '$login'");
$id1 = $select->fetch_object();
$sql1 = "INSERT INTO passwords (id, password) VALUES ( '".$id1->id."', '$password')";
mysqli_query($conn, $sql1);
$delete = "DELETE FROM reservations WHERE id = '$idR'";
mysqli_query($conn, $delete);
mysqli_close($conn);
echo json_encode($id1);
?>