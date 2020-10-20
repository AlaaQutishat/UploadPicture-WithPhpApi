<?php 
$connect=mysqli_connect("localhost","id14284811_root","Qutishat#698","id14284811_images");
if(isset($_POST['image'])){
	$target_dir="Images/";
	$image=$_POST['image'];
	$imagestore=rand()."_".time().".jpeg";
	
	$target_dir=$target_dir."/".$imagestore;
	file_put_contents($target_dir,base64_decode($image));
	$result=array();
	$finalurl="https://testpicture010.000webhostapp.com/Images/".$imagestore;
	$select="INSERT INTO image(url) VALUES ('$finalurl')";
	$responce=mysqli_query($connect,$select);
	if($responce){
		
		echo "Image Uploaded" ; 
		mysqli_close($connect);
		
	}
	else {
	    	echo "Failed" ; 
	}
	
	
	
}


?>