CREATE DEFINER=`root`@`localhost` PROCEDURE `select_by_title`(paramTitle varchar(100))
BEGIN
	select * from book
    where book.title = paramTitle limit 20;
END