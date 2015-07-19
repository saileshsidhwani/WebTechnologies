<!DOCTYPE html>

<html>
<head>
    
    <style>
        h1
        {
            font-size: 40px;
            align-items: center;
            text-align: center;
        }
        
        table
        {
            align-items: center
        }


        td
        {
            padding: 10px;
        }
        
    
    </style>
    
    <h1>
        <img src=" http://cs-server.usc.edu:45678/hw/hw6/ebay.jpg" style= "vertical-align:middle"><img>    
        Shopping
    </h1>
</head>

    
    
<center>
<body>
            <script>
        
        function validateNumber(name1,value1)
        {
            if(isNaN(value1) || value1 < 0)
            { 
                alert("'"+name1+"' should be a valid number greater than or equal to 0");
                return false;
            }
            
            return true;
    
        }        
                
                
        function validate()
        {
            var keywords = document.forms["shopping"]["keywords"].value;
            if(keywords.trim().length == 0)
            {
                alert("Enter value for Key Words.");
                return false;
            }
            var from ;
            var to ; 
            var handlingTime;
            var result = true;
            
            var name1 = document.forms["shopping"]["from"].name;
            from = document.forms["shopping"]["from"].value;
            //alert("from : " + from);
            result = validateNumber(name1,from);
            if(result == false)
                return false;
            document.forms["shopping"]["from"].text = from;
            
            name1 = document.forms["shopping"]["to"].name;
            to = document.forms["shopping"]["to"].value;
            //alert("to : " + to);
            result = validateNumber(name1,to);
            if(result == false)
                return false;
            
            {
                if(parseFloat(from) > parseFloat(to))
                {
                    alert("from:"+from+"....to:"+to);
                    alert("'from' should always be less than 'to'")
                    return false;
                }
            }
            
            name1 = document.forms["shopping"]["handlingTime"].name
            handlingTime = document.forms["shopping"]["handlingTime"].value;
            if(handlingTime.trim().length > 0)
            {
                result = validateNumber(name1,handlingTime);
                if(result == false)
                    return false;
                if(handlingTime % 1 != 0 || handlingTime == 0)
                {
                    alert("'Max Handling Time' should be an integer value greater than 0");
                    return false;
                }
            }
            d
            return true; 
            
        }
        
        

            
    </script>

    <form name="shopping" method="get" action="">
    
    <fieldset>
        
        <table rules="rows">
            <tr>
                <td style="font-weight:bold">Key Words* :</td>
                <td>    <input type="text" name="keywords" size=70 value="<?php if(isset($_GET["keywords"])) echo $_GET["keywords"] ?>"></td>
            </tr>
            
           <tr>
                <td style="font-weight:bold">Price Range :</td>
                <td>    from $ <input type="text" name="from" size=6 value="<?php if(isset($_GET["from"]))echo $_GET["from"]?>"> 
                          to $ <input type="text" name="to" size=6 value="<?php if(isset($_GET["to"])) echo $_GET["to"]?>"></td>
            </tr>
            
            <tr>
                <td style="font-weight:bold">Condition : </td>
                <td>    <input type="checkbox" name= "condition[]" <?php if(isset($_GET["condition"])){ if(in_array("1000", $_GET["condition"])) {echo "checked";}}?> value =1000> New
                        <input type="checkbox" name= "condition[]" <?php if(isset($_GET["condition"])){ if(in_array("3000", $_GET["condition"])) {echo "checked";}}?> value =3000> Used
                        <input type="checkbox" name= "condition[]" <?php if(isset($_GET["condition"])){ if(in_array("4000", $_GET["condition"])) {echo "checked";}}?> value =4000> Very Good
                        <input type="checkbox" name= "condition[]" <?php if(isset($_GET["condition"])){ if(in_array("5000", $_GET["condition"])) {echo "checked";}}?> value =5000> Good
                        <input type="checkbox" name= "condition[]" <?php if(isset($_GET["condition"])){ if(in_array("6000", $_GET["condition"])) {echo "checked";}}?> value =6000> Acceptable
                </td>
            </tr>
            
            <tr>
                <td style="font-weight:bold">Buying Formats : </td>
                <td>
                        <input type="checkbox" name= "buyingFormats[]" <?php if(isset($_GET["buyingFormats"])){ if(in_array("FixedPrice", $_GET["buyingFormats"])) {echo "checked";}}?> value ="FixedPrice"> Buy It Now
                        <input type="checkbox" name= "buyingFormats[]" <?php if(isset($_GET["buyingFormats"])){ if(in_array("Auction", $_GET["buyingFormats"])) {echo "checked";}}?> value="Auction"> Auction
                        <input type="checkbox" name= "buyingFormats[]" <?php if(isset($_GET["buyingFormats"])){ if(in_array("Classified", $_GET["buyingFormats"])) {echo "checked";}}?> value="Classified"> Classified Ads
                </td>
            </tr>
            
            <tr>
                <td style="font-weight:bold">Seller : </td>
                <td><input type="checkbox" name= "returnAccepted" <?php if(isset($_GET["returnAccepted"])) {echo "checked";}?>> Return Accepted</td>
            </tr>
            
            <tr>
                <td style="font-weight:bold">Shipping : </td>
                <td>
                        <input type="checkbox" name= "freeShipping" <?php if(isset($_GET["freeShipping"])) {echo "checked";}?>> Free Shipping <br>
                        <input type="checkbox" name= "expeditedShipping" <?php if(isset($_GET["expeditedShipping"])) {echo "checked";}?>> Expedited shipping available<br>
                        Max handling time (days): <input type="text" name= "handlingTime" size=3 value ="<?php if(isset($_GET["handlingTime"])) echo $_GET["handlingTime"] ?>">
                </td>
            </tr>
            
            <tr>
                <td style="font-weight:bold">Sort by : </td>
                <td>
                        <SELECT NAME="sortBy">
                        <OPTION <?php if(isset($_GET["search"]) && !strcmp($_GET["sortBy"],"BestMatch")) {echo "SELECTED";} ?> value="BestMatch" SELECTED > Best Match</OPTION>
                        <OPTION <?php if(isset($_GET["search"]) && !strcmp($_GET["sortBy"],"CurrentPriceHighest")){echo "SELECTED";} ?> value="CurrentPriceHighest"> Price : highest first</OPTION>
                        <OPTION <?php if(isset($_GET["search"]) && !strcmp($_GET["sortBy"],"PricePlusShippingHighest")){echo "SELECTED";} ?> value="PricePlusShippingHighest"> Price + Shipping : highest first</OPTION>
                        <OPTION <?php if(isset($_GET["search"]) && !strcmp($_GET["sortBy"],"PricePlusShippingLowest")) {echo "SELECTED";} ?> value="PricePlusShippingLowest"> Price + Shipping : lowest first</OPTION></SELECT>
                </td>
            </tr>
        
            <tr>
                <td style="font-weight:bold">Results Per Page : </td>
                <td>
                        <SELECT NAME="resultsPerPage">
                        <OPTION <?php if(isset($_GET["search"]) && $_GET["resultsPerPage"]==5) {echo "SELECTED";} ?> value=5 SELECTED> 5 </OPTION>
                        <OPTION <?php if(isset($_GET["search"]) && $_GET["resultsPerPage"]==10) {echo "SELECTED";} ?> value=10> 10 </OPTION>
                        <OPTION <?php if(isset($_GET["search"]) && $_GET["resultsPerPage"]==15) {echo "SELECTED";} ?> value=15> 15 </OPTION>
                        <OPTION <?php if(isset($_GET["search"]) && $_GET["resultsPerPage"]==20) {echo "SELECTED";} ?> value=20> 20 </OPTION></SELECT>
                </td>
            </tr>
        
        
        
        </table>
        
        <INPUT TYPE="reset" VALUE="clear" onclick = "window.location.href = 'http://cs-server.usc.edu:29084/hw6php.php';">
        <INPUT TYPE="submit" VALUE="search" name = "search" align="right" onclick= "return validate()">
        
    </fieldset>
    </form>
        
    <?php
        $keywords=$from=$to=$condition=$seller=$buyingFormats=$handlingTime=$sortBy=$resultsPerPage = "";
        $xml = "";
        $i = 0;
        $j = 0;
        $string = "";

        $url = "http://svcs.eBay.com/services/search/FindingService/v1?siteid=0&SECURITY-APPNAME=USC2af499-d2c4-4a3e-b4ca-e74b40930fc&OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=XML";

        function append(&$_string,&$_i,$_name,$_value,$_len)
        {
            $k = 0;
            $_string .= "&itemFilter($_i).name=".$_name;
            if($_len == 0)
            {
                $_string .= "&itemFilter($_i).value=".$_value;
            }
            else
            {
                for($k = 0; $k<$_len; $k++)
                {
                    $_string .= "&itemFilter($_i).value($k)=".$_value[$k];  
                }
            }
         
            $_i = $_i + 1;
            
        }

        function printTable()
        {
            $numOfResults = $i =0;
            global $xml,$keywords,$resultsPerPage;
            $numOfResults = $xml->paginationOutput->totalEntries;
            echo "<br><br>";
            if($numOfResults > 0)
            {
                echo "<table border=1 border-collapse=collapse><th colspan =2>Number of results for $keywords : $numOfResults</th>";                
                foreach($xml->searchResult->children() as $child)
                {
                    $condition = $child->condition->conditionDisplayName;
                    $buyingFormat = $child->listingInfo->listingType;
                    $handlingTime = $child->shippingInfo->handlingTime;
                    $expeditedShipping =$child->shippingInfo->expeditedShipping;
                    $price = $child->sellingStatus->convertedCurrentPrice;
                    $shippingCost = $child->shippingInfo->shippingServiceCost;
                    $topRatedImg = "http://cs-server.usc.edu:45678/hw/hw6/itemTopRated.jpg";
                    
                    echo "<tr><td><img src=$child->galleryURL></td>";
                    echo "<td><a href =$child->viewItemURL>$child->title</a><br>";
                    
                    if($condition != null)
                    {
                    if(!strcmp($child->topRatedListing,"true"))
                        echo "<b>Condition :</b> $condition <img src=$topRatedImg height=50 weight=50 style=vertical-align:middle><br>";
                    else
                         echo "<b>Condition :</b> $condition<br><br>";
                    }
                    
                    if(!strcmp($buyingFormat,"FixedPrice") || !strcmp($buyingFormat,"StoreInventory"))
                        $buyingFormat = "Buy It Now";
                    else if(!strcmp($buyingFormat,"Classified"))
                        $buyingFormat = "Classified Ad";
                    echo "<b>$buyingFormat</b><br><br>";
                    
                    if(!strcmp($child->returnsAccepted,"true"))
                        echo "Seller Accepts Return<br>";
                    
                    if($shippingCost!= null && $shippingCost == 0)
                    {
                        if(!strcmp($expeditedShipping,"true"))
                            echo "FREE Shipping -- Expedited Shipping Available -- Handled for shipping in $handlingTime day(s)<br><br>";
                        else
                            echo "FREE Shipping -- Handled for shipping in $handlingTime day(s)<br><br>";
                    }
                    else
                    {
                        if(!strcmp($expeditedShipping,"true"))
                            echo "Shipping Not FREE -- Expedited Shipping Available -- Handled for shipping in $handlingTime day(s)<br><br>";
                        else
                            echo "Shipping Not FREE -- Handled for shipping in $handlingTime day(s)<br><br>";
                        
                    }
                    
                    if($shippingCost!= null && $shippingCost > 0)
                    {
                        echo "<b>Price : $price (+ $$shippingCost for shipping)</b> <i>From $child->location</i><br>";
                    }
                    else
                    {
                       echo "<b>Price : $price </b> <i>From $child->location</i><br>"; 
                    }
                    echo "</td></tr>";
                    
               }
                echo "</table>";
            }
            else
                echo "<p>No Results Found";
        }


        function generateXML()
        {
            global $keywords,$from,$to,$condition,$seller,$buyingFormats,$handlingTime,$sortBy,$resultsPerPage,$url,$xml;
            $length = 0;
            $i = 0;
            $keyword = "";
            if(isset($_GET["search"]))
            {
                $keywords = $_GET["keywords"];
                $keyword = urlencode($keywords);
                $string = "&keywords=".$keyword;
                            
                if(isset($_GET["from"]) && $_GET["from"] != "")
                {
                    $from = $_GET["from"];
                    append($string,$i,"MinPrice",$from,0);
                }
            
                if(isset($_GET["to"]) && $_GET["to"] != "")
                {
                    $to = $_GET["to"];
                    append($string,$i,"MaxPrice",$to,0);
                }
                if(isset($_GET["condition"]))
                {
                    $condition = $_GET["condition"];
                    $length = count($condition);
                    append($string,$i,"Condition",$condition,$length);
                }
                if(isset($_GET["buyingFormats"]))
                {
                    $buyingFormats = $_GET["buyingFormats"];
                    $length = count($buyingFormats);
                    append($string,$i,"ListingType",$buyingFormats,$length);
                }
                if(isset($_GET["returnAccepted"]))
                    append($string,$i,"ReturnsAcceptedOnly","true",0);
                if(isset($_GET["freeShipping"]))
                    append($string,$i,"FreeShippingOnly","true",0);
                if(isset($_GET["expeditedShipping"]))
                    append($string,$i,"ExpeditedShippingType","Expedited",0);
                
                if(isset($_GET["handlingTime"]) && $_GET["handlingTime"] != "")
                {
                    $handlingTime = $_GET["handlingTime"];
                    append($string,$i,"MaxHandlingTime",$handlingTime,0);
                }
                $sortBy = $_GET["sortBy"];
                $string .= "&sortOrder=".$sortBy;
            
                $resultsPerPage = $_GET["resultsPerPage"];
                $string .= "&paginationInput.entriesPerPage=".$resultsPerPage;
                $url .= $string;
        
            

    
                $xml = simplexml_load_file($url);
                //print_r($xml);
                printTable();
                       
                

            }
        }

        generateXML();

    ?>
    

    
    
</body>



</html>