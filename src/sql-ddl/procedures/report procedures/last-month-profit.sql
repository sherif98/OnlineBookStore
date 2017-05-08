CREATE DEFINER=`root`@`localhost` PROCEDURE `last_month_profit`()
BEGIN
	select sum(sold.selling_price) as profit
    from sold
    where (DATEDIFF(CURDATE(), sold.selling_date) / 30) <= 1;
END