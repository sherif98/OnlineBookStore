CREATE DEFINER=`root`@`localhost` PROCEDURE `top_ten_selling_books`()
BEGIN
	set sql_safe_updates = 0;
	delete from sold
	where (DATEDIFF(CURDATE(), sold.selling_date) / 30) >= 3;
    set sql_safe_updates = 1;
    
    select book.ISBN, book.title, book.publisher_name, sum(sold.quantity) as sold_copies
    from sold join book on book.ISBN = sold.ISBN
    group by book.ISBN
    order by sold_copies DESC
    limit 10;
END