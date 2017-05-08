CREATE DEFINER=`root`@`localhost` PROCEDURE `select_by_category`(categ_id int)
BEGIN
	select * from book
    where book.category_id = categ_id limit 20;
END