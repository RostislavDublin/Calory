import {Component, OnInit} from '@angular/core';

export class TestData {
  id: number;
  name: string;
}

export class TestDataContainer {
  testData: TestData[];
}


@Component({
  selector: 'app-test-parent',
  templateUrl: './test-parent.component.html',
  styleUrls: ['./test-parent.component.css']
})
export class TestParentComponent implements OnInit {

  dataFromParent: TestDataContainer;

  constructor() {
    this.dataFromParent = {testData: [{id: 1, name: 'Alpha'}]};
  }

  ngOnInit() {
    setTimeout(() => {
      console.log('on timeout 1');
      // this.dataFromParent = {testData: [{id: 2, name: 'Beta'}]};
      this.dataFromParent.testData = [{id: 2, name: 'Beta'}, {id: 3, name: 'Gamma'}];
    }, 2000);

    setTimeout(() => {
      console.log('on timeout 2');
      // this.dataFromParent = {testData: [{id: 2, name: 'Beta'}]};
      this.dataFromParent.testData.push({id: 4, name: 'Delta'});
    }, 6000);

    console.log('onInit completed');
  }

}
