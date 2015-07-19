<?php
header('Content-Type:application/json');
header('Access-Control-Allow-Origin:*');

class main
{
  public $ack="";
  public $resultCount="";
  public $pageNumber="";
  public $itemCount="";
  public $item1=array();
}
class main1
{
      public $ack="";
}
    

class item
{
  public $basicInfo;
  public $sellerInfo;
  public $shippingInfo;

}

class basicInfo
{
  public $title="";
  public $viewItemURL="";
  public $galleryURL="";
  public $pictureURLSuperSize="";
  public $convertedCurrentPrice="";
  public $shippingServiceCost="";
  public $conditionDisplayName="";
  public $listingType="";
  public $location="";
  public $categoryName="";
  public $topRatedListing="";

}

class sellerInfo
{
  public $sellerUserName;
  public $feedbackScore;	
  public $positiveFeedbackPercent;
  public $feedbackRatingStar;
  public $topRatedSeller;
  public $sellerStoreName;
  public $sellerStoreURL;
}

class shippingInfo
{
  public $shippingType;
  public $shipToLocations;
  public $expeditedShipping;
  public $oneDayShippingAvailable;
  public $returnsAccepted;
  public $handlingTime;
}

$keywords=$from=$to=$condition=$seller=$buyingFormats=$handlingTime=$sortBy=$resultsPerPage = "";
        $xml = "";
        $i = 0;
        $j = 0;
        $string = "";

        $url = "http://svcs.eBay.com/services/search/FindingService/v1?siteid=0&SECURITY-APPNAME=USC2af499-d2c4-4a3e-b4ca-e74b40930fc&OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=XML&outputSelector[1]=SellerInfo&outputSelector[2]=PictureURLSuperSize&outputSelector[3]=StoreInfo";
        //echo $url;


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


function generateXML()
{
global $keywords,$from,$to,$condition,$seller,$buyingFormats,$handlingTime,$sortBy,$resultsPerPage,$url,$xml,$pageNumber;

$length = 0;
$i = 0;


/*if(isset($_GET["search"]))
{*/
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
        if(isset($_GET["returnsAccepted"]))
            append($string,$i,"ReturnsAcceptedOnly","true",0);
        if(isset($_GET["freeShipping"]))
            append($string,$i,"FreeShippingOnly","true",0);
        if(isset($_GET["expeditedShipping"]))
            append($string,$i,"ExpeditedShippingType","Expedited",0);
                
        if(isset($_GET["maxDays"]) && $_GET["maxDays"] != "")
        {
            $handlingTime = $_GET["maxDays"];
            append($string,$i,"MaxHandlingTime",$handlingTime,0);
        }
        $sortBy = $_GET["sortBy"];
        $string .= "&sortOrder=".$sortBy;
            
        $resultsPerPage = $_GET["resultsPerPage"];
        $string .= "&paginationInput.entriesPerPage=".$resultsPerPage;
        /*$string .= "&paginationInput.pageNumber=".$_GET["pageNumber"];*/
        $url .= $string;
        

        $xml = simplexml_load_file($url);
        //echo $url;
        //echo "<br>";
    }
/*}*/

function parseData()
{
    $numOfResults = $i =0;
    global $xml,$keywords,$resultsPerPage;
    $numOfResults = (string)$xml->paginationOutput->totalEntries;
    if((string)$xml->ack == "Failure" || $numOfResults == "0" )
    {
        $mainObj=new main1();
        $mainObj->ack="No results found";
        return json_encode($mainObj);
    }
    else
    {
        $mainObj=new main();
        $mainObj->ack=(string)$xml->ack;
        $mainObj->resultCount=(string)$xml->paginationOutput->totalEntries;
        $mainObj->pageNumber=(string)$xml->paginationOutput->pageNumber;
        $mainObj->itemCount=(string)$xml->paginationOutput->entriesPerPage;
        
        foreach($xml->searchResult->children() as $child)
        {
            $itemObj=new item();
            $itemObj->basicInfo=new basicInfo();
            $itemObj->sellerInfo=new sellerInfo();
            $itemObj->shippingInfo=new shippingInfo();
            
            //basicInfo class
            $itemObj->basicInfo->viewItemURL=(string)$child->viewItemURL;
            $itemObj->basicInfo->title=(string)$child->title;
            $itemObj->basicInfo->galleryURL=(string)$child->galleryURL;
            $itemObj->basicInfo->pictureURLSuperSize=(string)$child->pictureURLSuperSize;
            $itemObj->basicInfo->convertedCurrentPrice=(string)$child->sellingStatus->convertedCurrentPrice;
            $itemObj->basicInfo->shippingServiceCost=(string)$child->shippingInfo->shippingServiceCost;
            $itemObj->basicInfo->categoryName=(string)$child->primaryCategory->categoryName;
            $itemObj->basicInfo->topRatedListing=(string)$child->topRatedListing;
            $itemObj->basicInfo->conditionDisplayName=(string)$child->condition->conditionDisplayName;
            $itemObj->basicInfo->listingType=(string)$child->listingInfo->listingType;
            $itemObj->basicInfo->location=(string)$child->location;
            
            //sellerInfo class
            $itemObj->sellerInfo->positiveFeedbackPercent=(string)$child->sellerInfo->positiveFeedbackPercent;
            $itemObj->sellerInfo->sellerUserName=(string)$child->sellerInfo->sellerUserName;
            $itemObj->sellerInfo->feedbackScore=(string)$child->sellerInfo->feedbackScore;
            $itemObj->sellerInfo->sellerStoreName=(string)$child->storeInfo->storeName;
            $itemObj->sellerInfo->sellerStoreURL=(string)$child->storeInfo->storeURL;
            $itemObj->sellerInfo->feedbackRatingStar=(string)$child->sellerInfo->feedbackRatingStar;
            $itemObj->sellerInfo->topRatedSeller=(string)$child->sellerInfo->topRatedSeller;


            //shippingInfo class
            $itemObj->shippingInfo->oneDayShippingAvailable=(string)$child->shippingInfo->oneDayShippingAvailable;
            $itemObj->shippingInfo->shippingType=(string)$child->shippingInfo->shippingType;
            $itemObj->shippingInfo->expeditedShipping=(string)$child->shippingInfo->expeditedShipping;
            $itemObj->shippingInfo->expeditedShipping=(string)$child->shippingInfo->expeditedShipping;    
            $itemObj->shippingInfo->returnsAccepted=(string)$child->returnsAccepted;  
            $itemObj->shippingInfo->handlingTime=(string)$child->shippingInfo->handlingTime;
            
            foreach($child->shippingInfo->children() as $locations)
            {
                if($locations->getName() =="shipToLocations")
                {
                 $itemObj->shippingInfo->shipToLocations = $itemObj->shippingInfo->shipToLocations.$locations.',';   
                }
            }
            $itemObj->shippingInfo->shipToLocations = substr($itemObj->shippingInfo->shipToLocations,0,-1);
            
            
            $mainObj->item1[$i]=$itemObj;
            $i = $i+1;

        }
    return json_encode($mainObj);    
    }

    
    
}

generateXML();
echo parseData();

?>
    
