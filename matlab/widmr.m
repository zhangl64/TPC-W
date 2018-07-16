function [avg_dmr] = widmr(dat, prio)
%------------------------------------------------------------------------
% function [avg_dmr] = widmr(dat, prio)
%
% Calculates the average deadline miss ratio for the given priority class.
%
%------------------------------------------------------------------------

i = [wi_home, wi_prod, wi_sreq, wi_shop, wi_buyr, wi_ordi, wi_admr, wi_newp, wi_bess, wi_sres, wi_creg, wi_buyc, wi_ordd, wi_admc];

if (~exist('prio'))
  prio = 1;
end

% Find end of data.

maxC = 1;
for j=1:length(i)
  maxC = max(maxC, wirtcon(i(j)) * prio);
end
millsperi = maxC*10;

hx = (0:100)/100*maxC;

total = 0;
missed = 0;
for j=1:length(i)
  hy = zeros(1, 101);
  wh = dat.wirt{i(j)}.h;
  if (i(j)==wi_home) 
    wh(:,2) = wh(:,2) + dat.wirt{wi_init}.h(:,2);
  end
  for k=1:length(wh(:,2))
    b = min(floor(wh(k,1)/millsperi) + 1, 101);
    hy(b)=hy(b) + wh(k,2);    
  end
 
%  interaction = iname(i(j));
  deadline = wirtcon(i(j)) * prio;
  deadline_index = ceil(deadline/maxC * 100) + 1;
  indexed_deadline = hx(deadline_index);

  total = total + sum(hy);

  for k=deadline_index:101
    missed = missed + hy(k);
  end
end

avg_dmr = missed / total;
