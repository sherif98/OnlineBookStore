CREATE DEFINER=`root`@`localhost` PROCEDURE `promot_customer`(paramCustomerId int)
BEGIN
	update customer
    set customer.is_manager = true
    where customer.id = paramCustomerId;
END