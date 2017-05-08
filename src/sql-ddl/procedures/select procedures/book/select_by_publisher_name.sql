CREATE DEFINER=`root`@`localhost` PROCEDURE `select_by_publisher_name`(paramPublisherName varchar(100))
BEGIN
	select * from book join publisher on book.publisher_name = publisher.publisher_name
    where publisher.publisher_name = paramPublisherName limit 20;
END