/**
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @flow strict-local
 * @format
 * @oncall react_native
 */

'use strict';

import * as React from 'react';
import {Text} from 'react-native';
import ItemList from '../components/ItemList';
import {LIST_100_ITEMS} from '../components/itemData';
import type {ScrollEvent} from 'react-native/Libraries/Types/CoreEventTypes';

const {useCallback, useState} = React;
const ItemListMemo = React.memo(ItemList);

function ReRenderWithNonPureChildBadExample(): React.Node {
  const [scrollOffset, setScrollOffset] = useState(0);
  const onScroll = useCallback(
    (evt: ScrollEvent) => {
      setScrollOffset(evt.nativeEvent.contentOffset.x);
    },
    [setScrollOffset],
  );

  return (
    <>
      <Text>{`Scroll Offset X: ${scrollOffset}`}</Text>
      <ItemList data={LIST_100_ITEMS} onScroll={onScroll} />
    </>
  );
}

function ReRenderWithNonPureChildGoodExample(): React.Node {
  const [scrollOffset, setScrollOffset] = useState(0);
  const onScroll = useCallback(
    (evt: ScrollEvent) => {
      setScrollOffset(evt.nativeEvent.contentOffset.x);
    },
    [setScrollOffset],
  );

  return (
    <>
      <Text>{`Scroll Offset X: ${scrollOffset}`}</Text>
      <ItemListMemo data={LIST_100_ITEMS} onScroll={onScroll} />
    </>
  );
}

export default {
  title: 'List re-render due to not pure or memoized',
  description:
    'The List component is not pure in the bad example. Even though all props are not changed, it will still re-render when parent re-renders.',
  Bad: ReRenderWithNonPureChildBadExample,
  Good: ReRenderWithNonPureChildGoodExample,
};
