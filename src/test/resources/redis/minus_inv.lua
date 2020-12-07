local existed = redis.call('EXISTS', KEYS[1])
if existed == 1 then
    local n = redis.call('GET', KEYS[1])
    if n > "0" then
        local n1 = redis.call('DECRBY', KEYS[1], ARGV[1])
        if( n1 < 0) then
            redis.call('INCRBY', KEYS[1], ARGV[1])
            return 0
        else
            redis.call('SET', ARGV[2], '1')
            return 1
        end
    else
        redis.call('SET', ARGV[2], '0')
        return 0
    end
else
    redis.call('SET', ARGV[2], '-1')
    return -1
end