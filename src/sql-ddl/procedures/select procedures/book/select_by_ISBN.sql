CREATE DEFINER=`root`@`localhost` PROCEDURE `new_procedure`(bookId int)
BEGIN
	select * from book
    where book.ISBN = bookId limit 20;
END