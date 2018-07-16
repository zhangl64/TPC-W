function [n] = wirtcon(i)
%-------------------------------------------------------------------------
% function [n] = wirtcon(i)
%
% Returns the 90% WIRT contraint for a given interaction number.
%-------------------------------------------------------------------------

%'Initial Home', 'Admin Confirm', 'Admin Request', 'Best Sellers', 'Buy Confirm', 'Buy Request', 'Customer Regist.', 'Home'
%'New Products', 'Order Display', 'Order Inquiry', 'Product Detail', 'Search Request', 'Search Results', 'Shopping Cart'

% Official TPC-W Spec
% n = [1 20 1 5 5 1 2 1 5 2 1 1 1 10 1];

% Ours.
% n = [5 20 5 5 5 5 2 5 5 2 5 5 5 10 5];

% Single deadline class
n = [0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250 0.250];

% Official TPC-W Spec v1.8
%n = [3 20 3 5 5 3 3 3 5 3 3 3 3 10 3];

n = n(i);

