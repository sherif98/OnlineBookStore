CREATE DEFINER=`root`@`localhost` PROCEDURE `select_by_author`(paramAuthorName varchar(100))
BEGIN
	select * from book join author on book.ISBN = author.ISBN
    where author.author_name = paramAuthorName limit 20;
END