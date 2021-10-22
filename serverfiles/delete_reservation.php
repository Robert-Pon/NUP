<?php
$idR = $_POST['idR'];
$conn = mysqli_connect("server", "login", "password", "db");
$delete = "DELETE FROM reservations WHERE id = '$idR'";
mysqli_query($conn, $delete);
mysqli_close($conn);
?>